
package persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import modelo.productos.Producto;

public class ProductoJpaController {
    
   private final EntityManagerFactory emf;
   private final EntityManager em;
   


    public ProductoJpaController() {
        emf = Persistence.createEntityManagerFactory("Tienda_PU");
        em = emf.createEntityManager();
    }

    

    public void create(Producto producto) {        
         EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(producto);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } 
    }

    public void edit(Producto producto) throws Exception {
        try {
            em.getTransaction().begin();
            producto = em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction() != null) {
                em.getTransaction().rollback();
            }
            throw ex;
        }
    }

    public void destroy(Long id) throws Exception {
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId(); 
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Producto con id " + id + " no existe.", enfe);
            }
            em.remove(producto);
            em.getTransaction().commit();
    }

    public Producto find(Long id) {
            return em.find(Producto.class, id);
    }

    public List<Producto> findEntities() {
            em.getTransaction().begin();
            List<Producto> productos = em.createQuery("SELECT P FROM Producto AS P", Producto.class).getResultList();
            em.getTransaction().commit();
            return productos;

    }

}
