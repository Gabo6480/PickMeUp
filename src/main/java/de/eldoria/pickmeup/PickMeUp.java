package de.eldoria.pickmeup;

import de.eldoria.eldoutilities.localization.ILocalizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.pickmeup.commands.PickMeUpCommand;
import de.eldoria.pickmeup.config.Configuration;
import de.eldoria.pickmeup.listener.CarryListener;

import java.util.logging.Logger;

public class PickMeUp extends EldoPlugin {

    private boolean initialized;
    private Configuration configuration;

    @Override
    public void onEnable() {
        if (!initialized) {
            configuration = new Configuration(this);
            ILocalizer.create(this, "en_US", "de_DE");
            MessageSender.create(this, "§6[PMU]", '2', 'c');
            registerListener(new CarryListener(configuration));
            registerCommand("pickmeup", new PickMeUpCommand(this));
            initialized = true;
        } else {
            configuration.reload();
        }
        ILocalizer.getPluginLocalizer(this).setLocale(configuration.getLanguage());
    }

    public void onReload() {
        onDisable();
        onEnable();
    }

    public static Logger logger() {
        return getInstance(PickMeUp.class).getLogger();
    }
}
