package com.airpapier.config;

import com.airpapier.interfaces.Config;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public final class DatabaseConfig implements Config {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final String dbData;


    public DatabaseConfig(Dotenv dotenv) {
        this.dbUrl = dotenv.get("DATABASE_URL");
        this.dbUser = dotenv.get("DATABASE_USER");
        this.dbPassword =  dotenv.get("DATABASE_PASSWORD");
        this.dbData = dotenv.get("DATABASE");
    }

    public void validate() {
        if (dbUrl == null)
            throw new ConfigurationException("DBUrl is missing");
        if(dbUser == null)
            throw new ConfigurationException("DBUser is missing");
        if(dbPassword == null)
            throw new ConfigurationException("DBPassword is missing");
        if(dbData == null)
            throw new ConfigurationException("DB is missing") ;
        if(!dbUrl.startsWith("jdbc:"))
            throw new ConfigurationException("Something went wrong with the values int the .env file");

    }
}