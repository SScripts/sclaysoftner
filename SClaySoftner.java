package sclaysoftner;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;

import sclaysoftner.task.Task;
import sclaysoftner.task.bank.Bank;


@Script.Manifest(description = "OS Clay Softner", name = "SClaySoftner", properties = "topic=0;client=4;")



public class SClaySoftner extends PollingScript<org.powerbot.script.rt4.ClientContext> implements PaintListener {


    private long startTime;
    public static String status;
	
    public List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());
	
    @Override
    public void start() {
        startTime = System.currentTimeMillis();
        synchronized (tasks) {
            tasks.add(new Bank(ctx));

            tasks.notifyAll();
        }
        
    }
    
    @Override
    public void poll() {

        synchronized(tasks) {
            if (tasks.size() == 0) {
                try {
                    tasks.wait();
                } catch (InterruptedException ignored) {}
            }
        }

        for (Task task : tasks) {
            if (task.activate()) {
                task.execute();
            }
        }

    }
    
    public String formatTime(final long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }
    
    
    @Override
    public void repaint(Graphics g) {
    	g.setColor(Color.BLACK);
        g.drawString("Runtime:" + formatTime(getTotalRuntime()), 203, 416);
        g.drawString("Status:" + status, 203, 416);
    	
    	
    }
}
