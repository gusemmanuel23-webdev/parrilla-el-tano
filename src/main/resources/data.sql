-- Insertamos el usuario 'admin' con la contraseña 'admin123' (ya hasheada con BCrypt)
-- Si la tabla se llama 'usuarios' según tu entidad Usuario.java
INSERT INTO usuarios (username, password, enabled)
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uqqCyO', true)
    ON DUPLICATE KEY UPDATE username=username;