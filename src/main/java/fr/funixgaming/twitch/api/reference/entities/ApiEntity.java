package fr.funixgaming.twitch.api.reference.entities;

import com.google.gson.Gson;

public abstract class ApiEntity {
    @Override
    public String toString() {
        return this.toJson();
    }

    public String toJson() {
        return new Gson().toJson(this, this.getClass());
    }

    public abstract boolean equals(Object obj);
}
