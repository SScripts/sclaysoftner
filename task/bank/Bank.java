package sclaysoftner.task.bank;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import sclaysoftner.SClaySoftner;
import sclaysoftner.task.Task;


public class Bank extends Task {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    private final Area bankArea = new Area(
            new Tile(3180, 3441, 0),
            new Tile(3180, 3432, 0),
            new Tile(3186, 3432, 0),
            new Tile(3186, 3441, 0)
    );

    @Override
    public boolean activate() {

        return bankArea.contains(ctx.players.local().tile()) /* && ctx.inventory.select().count() == 28 && !ctx.bank.opened() */;
    }

    @Override
    public void execute() {

        GameObject bank = ctx.objects.id(17005).nearest().limit(2).shuffle().poll();
        if (ctx.bank.opened()){
            SClaySoftner.status = "Banking";
        } else if (bank.inViewport() && !ctx.bank.opened()){
            bank.interact("Bank");
            SClaySoftner.status = "Opening Bank";
            Condition.wait(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    if (ctx.bank.opened())
                        return true;
                    return false;
                }
            });
        } else ctx.camera.turnTo(bank);
    }
}