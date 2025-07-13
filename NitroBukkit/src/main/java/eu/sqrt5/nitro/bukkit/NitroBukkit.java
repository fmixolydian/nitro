package eu.sqrt5.nitro.bukkit;

import eu.sqrt5.nitro.core.LexedBlock;
import eu.sqrt5.nitro.core.Nitro;
import eu.sqrt5.nitro.core.CodeBlock;
import eu.sqrt5.nitro.core.SyntaxError;
import org.bukkit.plugin.java.JavaPlugin;

public final class NitroBukkit extends JavaPlugin {

    public Nitro nitro = new Nitro();

    @Override
    public void onEnable() {
        String prg = """
                on PlayerDeath {
                	changep lives -1;
                	if [calc [getp lives] == 0] then {
                		ban "You ran out of lives"
                	}
                }
                """;

        // Plugin startup logic
        try {
            LexedBlock L = new LexedBlock(prg);
            CodeBlock P = new CodeBlock(L);
        } catch (SyntaxError err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
