package club.sk1er.patcher.tweaker;

import club.sk1er.patcher.optifine.OptiFineGenerations;
import club.sk1er.patcher.optifine.OptiFineGenerations.Generation;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import gg.essential.universal.UDesktop;
import me.oondanomala.assential.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModCompatManager {
    private static final Logger LOGGER = LogManager.getLogger("Patcher - Mod Compatibility");

    public static boolean smoothFont = false;
    public static String optifineVersion = "NONE";
    public static Generation optifineGeneration = Generation.UNKNOWN;

    static void detectMods(Class<?> caller) {
        // SmoothFont
        ClassLoader classLoader = caller.getClassLoader();
        if (classLoader.getResource("bre/smoothfont/mod_SmoothFont.class") != null) {
            ModCompatManager.smoothFont = true;
            LOGGER.info("SmoothFont detected, disabling FontRenderer optimizations.");
        }
        // Optifine
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader("Config");
            classReader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
            for (FieldNode fieldNode : classNode.fields) {
                if (fieldNode.name.equals("OF_RELEASE")) {
                    optifineVersion = (String) fieldNode.value;
                    break;
                }
            }
            Set<String> knownSupportedOFVersions = ImmutableSet.of(
                "I7",
                "L5",
                "M5", "M6_pre1", "M6_pre2", "M6",
                "G5", "G6_pre1", "G6"
            );
            if (!knownSupportedOFVersions.contains(optifineVersion)) {
                // User's version is not known, fetch all optifine versions
                Set<String> supportedVersions = fetchSupportedOptiFineVersions();
                if (!supportedVersions.contains(optifineVersion)) {
                    LOGGER.error("User has outdated OptiFine. (version: OptiFine-{})", optifineVersion);
                    haltForOptiFine("Patcher has detected OptiFine " + optifineVersion + ", which is not supported and will crash.\n" +
                        "Please update to a supported version of OptiFine and try again.\n" +
                        "Supported versions: " + String.join(", ", supportedVersions));
                }
            }

            optifineGeneration = new OptiFineGenerations().getGeneration(optifineVersion);
            if (optifineGeneration == Generation.UNKNOWN) {
                // User's generation is not known, fetch all optifine generations
                String g = WebUtil.fetchString("https://raw.githubusercontent.com/Oondanomala/Patcher/zeroconfig/data/optifine_generations.json");
                if (g != null) {
                    optifineGeneration = new Gson().fromJson(g, OptiFineGenerations.class).getGeneration(optifineVersion);
                }
            }
        } catch (IOException | JsonParseException e) {
            // No optifine
        }
    }

    private static Set<String> fetchSupportedOptiFineVersions() {
        String v = WebUtil.fetchString("https://raw.githubusercontent.com/Oondanomala/Patcher/zeroconfig/data/optifine.txt");
        if (v == null) {
            return Collections.emptySet();
        }
        Set<String> versions = new HashSet<>();
        Collections.addAll(versions, v.split("\\R"));
        return versions;
    }

    private static void haltForOptiFine(String message) {
        if (GraphicsEnvironment.isHeadless()) {
            LOGGER.fatal("{}\nhttps://optifine.net/downloads", message);
            PatcherTweaker.invokeExit();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JButton openOptifine = new JButton("Open OptiFine Website");
        openOptifine.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    UDesktop.browse(new URI("https://optifine.net/downloads"));
                } catch (Exception ex) {
                    JLabel label = new JLabel();
                    label.setText("Failed to open OptiFine website.");
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    label.setAlignmentY(Component.CENTER_ALIGNMENT);
                }
            }
        });

        JButton close = new JButton("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PatcherTweaker.invokeExit();
            }
        });

        Object[] options = {openOptifine, close};
        JOptionPane.showOptionDialog(frame, message, "Launch Aborted", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        PatcherTweaker.invokeExit();
    }
}
