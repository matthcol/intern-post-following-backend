package canard.intern.post.following.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testAssociation(){
        var q = new Question();
        assertEquals(0, q.getChoices().size());
        // il se passe plein de choses ici
        var c5 = new Choice();
        q.getChoices().add(c5);
        System.out.println(q);
    }

}