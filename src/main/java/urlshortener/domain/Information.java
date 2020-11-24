package urlshortener.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class Information {
    public String name;
    public String description;
    public AtomicInteger number;

    public Information(String name, String description, AtomicInteger number) {
        this.name = name;
        this.description = description;
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AtomicInteger getNumber() {
        return number;
    }

    public void setNumber(AtomicInteger number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}