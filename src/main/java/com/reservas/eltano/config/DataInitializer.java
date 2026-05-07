package com.reservas.eltano.config;

import com.reservas.eltano.model.Usuario;
import com.reservas.eltano.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificamos si ya existe el admin para no duplicarlo cada vez que reinicies
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Encriptamos la contraseña "admin123" antes de guardar
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);

            usuarioRepository.save(admin);
            System.out.println(">>> Usuario Admin creado por defecto (admin / admin123)");
        }
    }
}