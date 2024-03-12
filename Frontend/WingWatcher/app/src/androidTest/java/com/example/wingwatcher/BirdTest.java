package com.example.wingwatcher;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class BirdTest {

    @Test
    public void testBirdInitialization() {
        int id = 1;
        String name = "Robin";
        String imageUrl = "https://example.com/robin.jpg";

        Bird bird = new Bird(id, name, imageUrl);

        assertEquals(id, bird.getId());
        assertEquals(name, bird.getName());
        assertEquals(imageUrl, bird.getImageUrl());
    }

    @Test
    public void testSetName() {
        Bird bird = new Bird(1, "Sparrow", "https://example.com/sparrow.jpg");

        String newName = "Blue Jay";
        bird.setName(newName);

        assertEquals(newName, bird.getName());
    }

    @Test
    public void testSetImageUrl() {
        Bird bird = new Bird(1, "Cardinal", "https://example.com/cardinal.jpg");

        String newImageUrl = "https://example.com/new_cardinal.jpg";
        bird.setImageUrl(newImageUrl);

        assertEquals(newImageUrl, bird.getImageUrl());
    }
}
