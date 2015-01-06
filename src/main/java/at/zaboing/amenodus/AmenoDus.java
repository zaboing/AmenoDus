package at.zaboing.amenodus;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = AmenoDus.MOD_ID, version = AmenoDus.MOD_VERSION)
public class AmenoDus {
	public static final String MOD_ID = "AmenoDus";
	public static final String MOD_VERSION = "0.0.1";
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		KeyBindings.init();
		try {
			FMLCommonHandler.instance().bus().register(new InputHandler());
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
