package at.zaboing.amenodus;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class KeyBindings {
	public static final String CATEGORY = "key.categories.amenodus";
	
	public static KeyBinding engage;
	
	
	public static void init() {
		engage = new KeyBinding("key.engage", Keyboard.KEY_N, CATEGORY);
		
		ClientRegistry.registerKeyBinding(engage);
	}
}
