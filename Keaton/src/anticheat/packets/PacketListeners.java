package anticheat.packets;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import anticheat.Keaton;
import anticheat.packets.events.PacketKeepAliveEvent;
import anticheat.packets.events.PacketKeepAliveEvent.PacketKeepAliveType;
import anticheat.packets.events.PacketKillauraEvent;
import anticheat.packets.events.PacketTypes;
import anticheat.user.User;
import anticheat.utils.CustomLocation;

public class PacketListeners {

	private HashSet<EntityType> enabled;

	public PacketListeners() {
		enabled = new HashSet<EntityType>();
		enabled.add(EntityType.valueOf("PLAYER"));

		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						PacketContainer packet = event.getPacket();
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
						try {
							Object playEntity = getNMSClass("PacketPlayInUseEntity");
							String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
							if (version.contains("1_7")) {
								if (packet.getHandle() == playEntity) {
									if (playEntity.getClass().getMethod("c") == null) {
										return;
									}
								}
							} else {
								if (packet.getHandle() == playEntity) {
									if (playEntity.getClass().getMethod("a") == null) {
										return;
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						EnumWrappers.EntityUseAction type;
						try {
							type = packet.getEntityUseActions().read(0);
						} catch (Exception ex) {
							return;
						}

						int entityId = (int) packet.getIntegers().read(0);
						Entity entity = null;
						if(player.getWorld().getEntities().size() > 0) {
							for (Entity entityentity : player.getWorld().getEntities()) {
								if (entityentity.getEntityId() == entityId) {
									entity = entityentity;
								}
							}
						}
						
						if(entity == null) {
							return;
						}
						if (type == EntityUseAction.ATTACK) {
							Bukkit.getServer().getPluginManager()
									.callEvent(new PacketKillauraEvent(player, entity, PacketTypes.USE));
						}
					}
				});
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		if (version.contains("1_7") || version.contains("1_8")) {
			ProtocolLibrary.getProtocolManager()
					.addPacketListener(new PacketAdapter(Keaton.getAC(),
							new PacketType[] { PacketType.Play.Server.SPAWN_ENTITY_LIVING,
									PacketType.Play.Server.NAMED_ENTITY_SPAWN,
									PacketType.Play.Server.ENTITY_METADATA }) {

						@Override
						public void onPacketSending(PacketEvent event) {
							PacketContainer packet = event.getPacket();
							Entity e = (Entity) packet.getEntityModifier(event).read(0);
							if (e instanceof LivingEntity && enabled.contains((Object) e.getType())
									&& packet.getWatchableCollectionModifier().read(0) != null
									&& e.getUniqueId() != event.getPlayer().getUniqueId()) {
								packet = packet.deepClone();
								event.setPacket(packet);
								if (event.getPacket().getType() == PacketType.Play.Server.ENTITY_METADATA) {
									WrappedDataWatcher watcher = new WrappedDataWatcher(
											packet.getWatchableCollectionModifier().read(0));
									this.processDataWatcher(watcher);
									packet.getWatchableCollectionModifier().write(0,
											(List<WrappedWatchableObject>) watcher.getWatchableObjects());
								}
							}
						}

						private void processDataWatcher(WrappedDataWatcher watcher) {
							if (watcher != null && watcher.getObject(6) != null && watcher.getFloat(6) != 0.0F) {
								watcher.setObject(6, 1.0f);
							}
						}
					});
		}
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.POSITION }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
		                double posX = event.getPacket().getDoubles().read(0);
		                double posY = event.getPacket().getDoubles().read(1);
		                double posZ = event.getPacket().getDoubles().read(2);
		                float yaw2 = event.getPacket().getFloat().read(0).floatValue();
		                float pitch2 = event.getPacket().getFloat().read(1).floatValue();
		                CustomLocation customLocation4 = new CustomLocation(posX, posY, posZ, yaw2, pitch2);
		                Keaton.getUserManager().getUser(player.getUniqueId()).addMovePacket(customLocation4);
						Bukkit.getServer().getPluginManager().callEvent(new anticheat.packets.events.PacketEvent(player, Keaton.getUserManager().getUser(player.getUniqueId()).getLastlocation() != null ?  Keaton.getUserManager().getUser(player.getUniqueId()).getLastlocation() : player.getLocation(), player.getLocation(), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.POSITION));
		                Keaton.getUserManager().getUser(player.getUniqueId()).setLastlocation(player.getLocation());
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Server.POSITION }) {
					@Override
					public void onPacketSending(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}

						User user = anticheat.Keaton.getUserManager().getUser(player.getUniqueId());
						if (user != null) {
							user.setPosPacket(user.getPosPackets() + 1);
						}
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.KEEP_ALIVE }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
						Bukkit.getServer().getPluginManager()
								.callEvent(new PacketKeepAliveEvent(player, PacketKeepAliveType.CLIENT));
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Server.KEEP_ALIVE }) {
					@Override
					public void onPacketSending(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
						Bukkit.getServer().getPluginManager()
								.callEvent(new PacketKeepAliveEvent(player, PacketKeepAliveType.SERVER));
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
		                double posX = event.getPacket().getDoubles().read(0);
		                double posY = event.getPacket().getDoubles().read(1);
		                double posZ = event.getPacket().getDoubles().read(2);
		                float yaw2 = event.getPacket().getFloat().read(0).floatValue();
		                float pitch2 = event.getPacket().getFloat().read(1).floatValue();
		                CustomLocation customLocation4 = new CustomLocation(posX, posY, posZ, yaw2, pitch2);
		                Keaton.getUserManager().getUser(player.getUniqueId()).addMovePacket(customLocation4);
						Bukkit.getServer().getPluginManager()
								.callEvent(new anticheat.packets.events.PacketEvent(player,
										player.getLocation(), player.getLocation(), event.getPacket().getFloat().read(0), event.getPacket().getFloat().read(1),
										PacketTypes.POSLOOK));
					}
				});
		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.LOOK }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
		                double posX = event.getPacket().getDoubles().read(0);
		                double posY = event.getPacket().getDoubles().read(1);
		                double posZ = event.getPacket().getDoubles().read(2);
		                float yaw2 = event.getPacket().getFloat().read(0).floatValue();
		                float pitch2 = event.getPacket().getFloat().read(1).floatValue();
		                CustomLocation customLocation4 = new CustomLocation(posX, posY, posZ, yaw2, pitch2);
		                Keaton.getUserManager().getUser(player.getUniqueId()).addMovePacket(customLocation4);
						Bukkit.getServer().getPluginManager().callEvent(new anticheat.packets.events.PacketEvent(player,
								null, null, player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.POSITION));
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.ARM_ANIMATION }) {
					public void onPacketReceiving(final PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
						Bukkit.getServer().getPluginManager()
								.callEvent(new PacketKillauraEvent(player, null, PacketTypes.SWING));
					}
				});
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(Keaton.getAC(), new PacketType[] { PacketType.Play.Client.FLYING }) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						Player player = event.getPlayer();
						if (player == null) {
							return;
						}
						Bukkit.getServer().getPluginManager().callEvent(new anticheat.packets.events.PacketEvent(player,
								null, null, player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.FLYING));
					}
				});
	}

	public Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
