package coms309.birds;

public class Bird {
    private String species;
    private String scientificName;
    private String description;

    public Bird()
    {

    }

    public Bird(String species, String scientificName, String description)
    {
        this.species = species;
        this.scientificName = scientificName;
        this.description = description;
    }

    public String getSpecies()
    {
        return this.species;
    }

    public void setSpecies(String species)
    {
        this.species = species;
    }

    public String getScientificName()
    {
        return this.scientificName;
    }

    public void setScientificName(String scientificName)
    {
        this.scientificName = scientificName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString() {
        return species + " " + scientificName + " " + description;
    }
}
