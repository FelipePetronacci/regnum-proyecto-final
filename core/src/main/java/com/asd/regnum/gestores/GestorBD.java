package com.asd.regnum.gestores;

import java.sql.*;
import java.time.LocalDate;

public class GestorBD {
    private Connection connection;
    private final String dbName = "regnum_data.db";

    public void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            System.out.println("Base de datos conectada correctamente.");

            crearTablas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarUsuarios() {
        String sql = "SELECT idUsuario, Nombre, UltimoTiempo, PartidasJugadas FROM Usuarios;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== LISTA DE USUARIOS REGISTRADOS ===");

            while (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                String nombre = rs.getString("Nombre");
                String ultimoTiempo = rs.getString("UltimoTiempo");
                int partidasJugadas = rs.getInt("PartidasJugadas");

                System.out.println("ID Usuario: " + idUsuario +
                    " | nombre: " + nombre +
                    " | ultimo tiempo: " + ultimoTiempo +
                    " | partidas jugadas: " + partidasJugadas);
            }
            System.out.println("=====================================");

        } catch (SQLException e) {
            System.err.println("Error al consultar los usuarios en la base de datos:");
            e.printStackTrace();
        }
    }

    public int crearRegistroUsuario(String nombre, String ultimoTiempo) {
        String nombreMayus = nombre.toUpperCase();

        String sql = "INSERT INTO Usuarios (Nombre, UltimoTiempo, PartidasJugadas) " +
            "VALUES (?, ?, 1) " +
            "ON CONFLICT(Nombre) DO UPDATE SET " +
            "UltimoTiempo = excluded.UltimoTiempo, " +
            "PartidasJugadas = PartidasJugadas + 1;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombreMayus);
            pstmt.setString(2, ultimoTiempo);
            pstmt.executeUpdate();
            System.out.println("¡Usuario procesado correctamente en la BD: " + nombreMayus + "!");

        } catch (SQLException e) {
            System.err.println("Error al procesar el usuario en la base de datos:");
            e.printStackTrace();
        }

        return obtenerIdUsuarioPorNombre(nombreMayus);
    }

    private int obtenerIdUsuarioPorNombre(String nombre) {
        String sql = "SELECT idUsuario FROM Usuarios WHERE Nombre = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idUsuario");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void cargarPartida(int nivel, String nombre, String tiempoJugador) {
        String nombreMayus = nombre.toUpperCase();

        int idUsuario = crearRegistroUsuario(nombreMayus, tiempoJugador);

        if (idUsuario != -1) {
            String sqlPartida = "INSERT INTO Partidas (Nivel, Tiempo, Fecha, idUsuario) VALUES (?, ?, datetime('now'), ?);";

            try (PreparedStatement pstmt = connection.prepareStatement(sqlPartida)) {
                pstmt.setInt(1, nivel);
                pstmt.setString(2, tiempoJugador);
                pstmt.setInt(3, idUsuario);

                pstmt.executeUpdate();
                System.out.println("¡Partida registrada con éxito para el usuario " + nombreMayus + "!");
            } catch (SQLException e) {
                System.err.println("Error al guardar la partida:");
                e.printStackTrace();
            }
        }
    }

    private void crearTablas() {
        try (Statement stmt = connection.createStatement()) {

            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nombre TEXT UNIQUE NOT NULL, " +
                "UltimoTiempo TEXT, " +
                "PartidasJugadas INTEGER DEFAULT 0);";
            stmt.execute(sqlUsuarios);

            String sqlPartidas = "CREATE TABLE IF NOT EXISTS Partidas (" +
                "idPartida INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nivel INTEGER DEFAULT 0, " +
                "Tiempo TEXT, " +
                "Fecha TEXT, " +
                "idUsuario INTEGER, " +
                "FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE);";
            stmt.execute(sqlPartidas);

            System.out.println("Tablas 'Usuarios' y 'Partidas' creadas/verificadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
