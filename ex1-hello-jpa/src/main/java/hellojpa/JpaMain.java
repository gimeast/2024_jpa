package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Movie movie = new Movie();

            movie.setDirector("A");
            movie.setActor("B");
            movie.setName("바람과 함께 사라지다");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();;

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println(findMovie);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
