package me.oondanomala.essential;

import gg.essential.universal.UMinecraft;
import gg.essential.universal.UResolution;
import gg.essential.universal.USound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public final class Notifications {
    static final Notifications INSTANCE = new Notifications();
    private static final Deque<Notification> notifications = new LinkedList<>();
    private static long lastTime = -1;
    private static long deltaTime;

    private Notifications() {
    }

    public static void push(String title, String message) {
        push(title, message, 5, () -> {});
    }

    public static void push(String title, String message, float duration) {
        push(title, message, duration, () -> {});
    }

    public static void push(String title, String message, Runnable action) {
        push(title, message, 5, action);
    }

    public static void push(String title, String message, float duration, Runnable action) {
        notifications.add(new Notification(title, message, duration, action));
    }

    @SubscribeEvent
    public void drawNotifications(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (lastTime == -1) lastTime = UMinecraft.getTime();
            else {
                long currentTime = UMinecraft.getTime();
                deltaTime = currentTime - lastTime;
                lastTime = currentTime;
            }
        } else if (event.phase == TickEvent.Phase.END) {
            int yOffset = 0;
            for (Iterator<Notification> iterator = notifications.iterator(); iterator.hasNext(); ) {
                Notification notification = iterator.next();
                if (yOffset + notification.height + 5 < UResolution.getScaledHeight()) {
                    notification.draw(deltaTime, yOffset);
                }
                yOffset += notification.height + 5;
                if (notification.isExpired()) {
                    iterator.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            for (Iterator<Notification> iterator = notifications.iterator(); iterator.hasNext();) {
                Notification notification = iterator.next();
                if (notification.hovered) {
                    USound.INSTANCE.playButtonPress();
                    notification.clickAction.run();
                    iterator.remove();
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }
}
