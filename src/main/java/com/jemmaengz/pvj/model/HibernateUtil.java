package com.jemmaengz.pvj.model;

import java.util.HashMap;
import java.util.Map;

import com.jemmaengz.pvj.Utilities;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
  private static StandardServiceRegistry registry;
  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      try {
        StandardServiceRegistryBuilder registryBuilder = 
            new StandardServiceRegistryBuilder();

        Map<String, String> settings = new HashMap<>();
        System.out.println(Utilities.dotenv.get("DB_URL"));
        settings.put("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver");
        settings.put("hibernate.connection.url", Utilities.dotenv.get("DB_URL"));
        settings.put("hibernate.connection.username", Utilities.dotenv.get("DB_USER"));
        settings.put("hibernate.connection.password", Utilities.dotenv.get("DB_PASSWORD"));
        settings.put("hibernate.show_sql", Utilities.dotenv.get("PVJ_ENV", "").equals("prod") ? "false" : "true");
        settings.put("hibernate.format_sql", Utilities.dotenv.get("PVJ_ENV", "").equals("prod") ? "false" : "true");
        settings.put("hibernate.hbm2ddl.auto", "update");

        registryBuilder.applySettings(settings);

        registry = registryBuilder.build();

        MetadataSources sources = new MetadataSources(registry)
            .addAnnotatedClass(Usuario.class)
            .addAnnotatedClass(Departamento.class)
            .addAnnotatedClass(Proveedor.class)
            .addAnnotatedClass(Compra.class)
            .addAnnotatedClass(Producto.class)
            .addAnnotatedClass(Venta.class)
            .addAnnotatedClass(VentaDetalle.class)
            .addAnnotatedClass(HistorialSesion.class)
            .addAnnotatedClass(Metadatos.class);

        Metadata metadata = sources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
      } catch (Exception e) {
        System.out.println("SessionFactory creation failed");
        if (registry != null) {
          StandardServiceRegistryBuilder.destroy(registry);
        }
      }
    }
    return sessionFactory;
  }

  public static void shutdown() {
    if (registry != null) {
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }
}
