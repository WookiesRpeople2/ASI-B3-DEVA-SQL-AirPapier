package com.airpapier.config;

import com.airpapier.interfaces.Config;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public final class PortConfig implements Config {
    private final int portNumber;

    public PortConfig(Dotenv dotenv){
        this.portNumber = Integer.parseInt(dotenv.get("PORT"));
    }

    public void validate() {
       if(portNumber <= 0){
           throw new ConfigurationException("PortNumber is missing");
       }
    }
}
