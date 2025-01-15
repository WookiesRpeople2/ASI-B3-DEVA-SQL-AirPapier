package com.airpapier;
import com.airpapier.config.DatabaseConfig;
import com.airpapier.config.PortConfig;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public class Config {
    @Getter static final Config instance = new Config();
    private final DatabaseConfig db;
    private final PortConfig port;

    private Config() {
        Dotenv dotenv = Dotenv.configure()
                        .directory("../../assets/.env")
                        .filename(".env")
                        .load();
        this.db = new DatabaseConfig(dotenv);
        this.port = new PortConfig(dotenv);
        validate();
    }

    private void validate() {
        db.validate();
        port.validate();
    }
}