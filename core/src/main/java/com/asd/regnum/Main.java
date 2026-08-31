package com.asd.regnum;
import com.asd.regnum.menu.MainMenuScreen;
import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
