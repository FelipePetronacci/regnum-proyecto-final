
package com.asd.regnum.menu.configuracion;

import com.asd.regnum.Main;
import com.asd.regnum.gestores.GestorBD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ConfiguracionBD implements Screen {

    final Main game;
    Stage stage;

    BitmapFont font;
    BitmapFont titleFont;

    Skin skin;
    TextField nombreField;

    private Texture fondoTexture;

    public ConfiguracionBD(Main game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480));

        fondoTexture = new Texture(
            Gdx.files.internal("menu/menu_configbd.png")
        );

        Image fondo = new Image(fondoTexture);
        fondo.setFillParent(true);
        fondo.setScaling(Scaling.stretch);

        stage.addActor(fondo);

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                Gdx.files.internal("fuentes/determination.ttf")
            );

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        titleParam.size = 28;
        titleParam.color = Color.YELLOW;

        titleFont = generator.generateFont(titleParam);

        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 18;
        param.color = Color.WHITE;

        font = generator.generateFont(param);

        generator.dispose();

        TextButton.TextButtonStyle textButtonStyle =
            new TextButton.TextButtonStyle();

        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.YELLOW;

        skin = new Skin(
            Gdx.files.internal("skin/uiskin.json")
        );

        Label.LabelStyle titleStyle =
            new Label.LabelStyle(titleFont, Color.YELLOW);

        TextField.TextFieldStyle fieldStyle =
            new TextField.TextFieldStyle(
                skin.get(TextField.TextFieldStyle.class)
            );

        fieldStyle.font = font;
        fieldStyle.fontColor = Color.WHITE;
        fieldStyle.messageFont = font;

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().left().padTop(35).padLeft(50);

        stage.addActor(mainTable);

        mainTable.add(
            new Label("Configuracion de Base de Datos", titleStyle)
        ).left().padBottom(30).row();

        Table table = new Table();
        table.setFillParent(true);
        table.left().padLeft(50);

        stage.addActor(table);

        TextButton borrarTodosButton =
            new TextButton("Borrar todos los registros", textButtonStyle);

        TextButton borrarUsuarioButton =
            new TextButton("Borrar usuario", textButtonStyle);

        TextButton volverButton =
            new TextButton("Volver", textButtonStyle);

        nombreField = new TextField("", fieldStyle);
        nombreField.setMessageText("Nombre del usuario");

        borrarTodosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmarBorradoTotal();
            }
        });

        borrarUsuarioButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmarBorradoUsuario();
            }
        });

        volverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuConfiguracion(game));
                dispose();
            }
        });

        table.add(borrarTodosButton)
            .width(350)
            .height(45)
            .left()
            .padBottom(20)
            .row();

        table.add(nombreField)
            .width(350)
            .height(40)
            .left()
            .padBottom(10)
            .row();

        table.add(borrarUsuarioButton)
            .width(350)
            .height(45)
            .left()
            .padBottom(20)
            .row();

        table.add(volverButton)
            .width(200)
            .height(40)
            .left()
            .row();

        Gdx.input.setInputProcessor(stage);
    }

    private void confirmarBorradoTotal() {

        Dialog dialogo = new Dialog("Confirmar eliminacion", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    borrarTodosLosRegistros();
                }
            }
        };

        dialogo.text("¿Estas seguro de que queres borrar todos los registros?");
        dialogo.button("Cancelar", false);
        dialogo.button("Eliminar", true);
        dialogo.show(stage);
    }

    private void confirmarBorradoUsuario() {

        String nombre = nombreField.getText().trim();

        if (nombre.isEmpty()) {
            mostrarMensaje("Ingresa un nombre de usuario.");
            return;
        }

        Dialog dialogo = new Dialog("Confirmar eliminacion", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    borrarUsuario(nombre);
                }
            }
        };

        dialogo.text(
            "¿Estas seguro de que queres eliminar al usuario "
                + nombre + "?"
        );

        dialogo.button("Cancelar", false);
        dialogo.button("Eliminar", true);
        dialogo.show(stage);
    }

    private void borrarTodosLosRegistros() {

        GestorBD gestorBD = new GestorBD();

        try {
            gestorBD.conectar();
            gestorBD.eliminarTodosLosRegistros();

            mostrarMensaje(
                "Se ejecuto el borrado de todos los registros."
            );

        } catch (Exception e) {
            e.printStackTrace();

            mostrarMensaje(
                "No se pudieron eliminar los registros."
            );

        } finally {
            gestorBD.desconectar();
        }
    }

    private void borrarUsuario(String nombre) {

        GestorBD gestorBD = new GestorBD();

        try {
            gestorBD.conectar();

            boolean eliminado =
                gestorBD.eliminarUsuarioPorNombre(nombre);

            if (eliminado) {

                mostrarMensaje("Usuario eliminado correctamente.");
                nombreField.setText("");

            } else {

                mostrarMensaje("No se encontro ese usuario.");
            }

        } catch (Exception e) {
            e.printStackTrace();

            mostrarMensaje(
                "No se pudo eliminar el usuario."
            );

        } finally {
            gestorBD.desconectar();
        }
    }

    private void mostrarMensaje(String mensaje) {

        Dialog dialogo = new Dialog("Informacion", skin);

        dialogo.text(mensaje);
        dialogo.button("Aceptar");

        dialogo.show(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(
            Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)
        );

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

        stage.dispose();

        font.dispose();
        titleFont.dispose();

        skin.dispose();

        if (fondoTexture != null) {
            fondoTexture.dispose();
        }
    }
}
