package at.zaboing.amenodus;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InputHandler extends Robot {

	public InputHandler() throws AWTException {
		super();
	}

	public static final int MAX_RANGE_SQ = 25;

	private Entity target;

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (KeyBindings.engage.isPressed()) {
			List entities = mc.thePlayer.worldObj
					.getEntitiesWithinAABBExcludingEntity(mc.thePlayer,
							AxisAlignedBB.fromBounds(mc.thePlayer.posX - 5,
									mc.thePlayer.posY - 5,
									mc.thePlayer.posZ - 5,
									mc.thePlayer.posX + 5,
									mc.thePlayer.posY + 5,
									mc.thePlayer.posZ + 5));
			Entity closest = null;
			for (Object o : entities) {
				if (o instanceof EntityLiving) {
					Entity entity = (Entity) o;
					if (closest == null) {
						closest = entity;
					} else {
						if (distanceSq(entity, mc.thePlayer) < distanceSq(
								closest, mc.thePlayer)) {
							closest = entity;
						}
					}
				}
			}
			if (closest != null) {
				lookAt(closest, mc.thePlayer);
				mc.playerController.attackEntity(mc.thePlayer, closest);
				target = closest;
			}
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null) {
			target = null;
		}
		if (target != null) {
			lookAt(target, mc.thePlayer);
			if (distanceSq(target, mc.thePlayer) > MAX_RANGE_SQ) {
				InventoryPlayer inventory = mc.thePlayer.inventory;
				ItemStack current = inventory
						.getStackInSlot(inventory.currentItem);
				if (current == null || current.getItem() != Items.bow) {
					ItemStack[] inv = inventory.mainInventory;
					for (int i = 0; i < 9; i++) {
						if (inv[i] != null && inv[i].getItem() == Items.bow) {
							inventory.currentItem = i;
							break;
						}
					}
					for (int i = inv.length - 1; i >= 9; --i) {
						if (inv[i] != null && inv[i].getItem() == Items.bow) {
							ItemStack temp = inv[i];
							inv[i] = inventory
									.getStackInSlot(inventory.currentItem);
							inventory.setInventorySlotContents(
									inventory.currentItem, temp);
							break;
						}
					}
				}
				current = inventory.getStackInSlot(inventory.currentItem);
				if (current != null && current.getItem() == Items.bow) {
					if (!mc.thePlayer.isUsingItem()) {
						mousePress(java.awt.event.InputEvent.BUTTON3_MASK);
					}
				}
			} else {
				mc.playerController.attackEntity(mc.thePlayer, target);
			}
			if (target.isDead) {
				target = null;
			}
		}
	}

	private double distanceSq(Entity a, Entity b) {
		double dX = b.posX - a.posX;
		double dY = b.posY - a.posY;
		double dZ = b.posZ - a.posZ;
		return dX * dX + dY * dY + dZ * dZ;
	}

	private void lookAt(Entity e, EntityPlayer me) {
		double dirx = me.posX + me.width / 2 - e.posX - e.width / 2;
		double diry = me.posY + me.getEyeHeight() - e.posY - e.height / 2;
		double dirz = me.posZ + me.width / 2 - e.posZ - e.width / 2;

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		// to degree
		pitch = pitch * 180.0 / Math.PI;
		yaw = yaw * 180.0 / Math.PI;

		yaw += 90f;
		me.rotationPitch = (float) pitch;
		me.rotationYaw = (float) yaw;
	}
}
