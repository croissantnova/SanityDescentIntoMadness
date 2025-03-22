package croissantnova.sanitydim.util;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.PriorityQueue;
import java.util.UUID;

// https://github.com/Spaxterr/lynxlib/blob/e4de122d44abd912a37e4a43f351306db4a129aa/src/main/java/dev/spaxter/lynxlib/task/TaskScheduler.java#L11
@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TaskScheduler {

    private static final PriorityQueue<DelayedTask> taskQueue = new PriorityQueue<>();

    public static void scheduleTask(MinecraftServer server, float delay, Runnable task) {
        TaskScheduler.scheduleTask(server, delay, task, UUID.randomUUID());
    }

    public static void scheduleTask(MinecraftServer server, float delay, Runnable task, UUID taskId) {
        long currentTick = server.getTickCount();
        long executionTick = currentTick + (long)delay;

        if (delay == 0) {
            task.run();
        }
        else {
            DelayedTask delayedTask = new DelayedTask(executionTick, task, taskId);
            taskQueue.add(delayedTask);
        }
    }

    public static void unscheduleTask(UUID taskId) {
        taskQueue
                .stream()
                .filter(t -> t.id.equals(taskId))
                .toList()
                .forEach(taskQueue::remove);
    }

    public static void repeatTask(final MinecraftServer server, final float delay, final Runnable task, final UUID taskId) {
        Runnable repeatedTask = new Runnable() {
            @Override
            public void run() {
                task.run();
                TaskScheduler.scheduleTask(server, delay, this, taskId);
            }
        };

        TaskScheduler.scheduleTask(server, delay, repeatedTask, taskId);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        synchronized (taskQueue) {
            if (event.phase == TickEvent.Phase.END && !taskQueue.isEmpty()) {
                long currentTick = ServerHelper.server.getTickCount();

                while (!taskQueue.isEmpty() && taskQueue.peek().executionTick <= currentTick) {
                    DelayedTask taskToRun = taskQueue.poll();
                    taskToRun.task.run();
                }
            }
        }
    }


    private record DelayedTask(long executionTick, Runnable task, UUID id) implements Comparable<DelayedTask> {

        @Override
        public int compareTo(DelayedTask otherTask) {
            return Long.compare(this.executionTick, otherTask.executionTick);
        }
    }
}
