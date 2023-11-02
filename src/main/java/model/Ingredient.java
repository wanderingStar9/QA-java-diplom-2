package model;

public class Ingredient {
    private String _id;
    private String name;
    private String type;

    public Ingredient(String _id, String name, String type) {
        this._id = _id;
        this.name = name;
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
