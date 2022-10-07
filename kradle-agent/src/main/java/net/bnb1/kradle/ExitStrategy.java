package net.bnb1.kradle;

public class ExitStrategy implements ChangeStrategy {

    @Override
    public void onChangeDetected() {
        System.exit(0);
    }
}
