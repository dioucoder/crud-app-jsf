/**
 * 
 */
package com.database.operation.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

// OBJET ETUDIANT DEPUIS LE PACKAGE com.ui.operation.crud 
import com.ui.operation.crud.Etudiant;

// BEANS
@SuppressWarnings("deprecation") // Version dépréciée
@ManagedBean
@RequestScoped
/**
 * 
 */
public class EtudiantBean {
	private Etudiant etudiant;
// LA VARIABLE connexion ACCESSIBLE PAR TOUTES LES METHODES.
	Connection connexion;
	private Map<String, Object> _SESSION = FacesContext.getCurrentInstance().getExternalContext().getSessionMap(); // SESSION GLOBALE
	private ArrayList<Etudiant> listEtudiants; // LISTE DES ETUDIANTS QUI VA CONTENIR LES DONNEES D'ETUDIANTS
	
	public EtudiantBean() {
		// TODO Auto-generated constructor stub
		
		// Instancier la prop Etudiant pour qu'elle accessible à la vue
		etudiant = new Etudiant();
	}
	 
	 
	 
// ********************************************************************************************************************************
// **************************************__ETABLISSEMENT DE LA CONNEXION A LA BASE DE DONNEES | METHODE A APPELER POUR CHAQUE INTERROGATION DE LA BD__**************************************
	    public Connection connexionDB() {
	        // Contacter la base de données
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver"); // Le driver com.mysql.jdbc.Driver est déprécié et est remplacé par : com.mysql.cj.jdbc.Driver
	            connexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bdcrudjsf", "root", "");
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	            System.out.println("ECHEC : Impossible de se connecter a la base de donnees.");
	        }
	        return connexion; // Retourner la connexion
	    }
	
	    
	        
// ********************************************************************************************************************************
// **************************************__FETCHER SUR TOUTES LES DONNES DE LA BD POUR LES AFFICHER AU NIVEAU DE LA VUE__**************************************
	    public List<Etudiant> listEtudiants() {
	        try {
	            listEtudiants = new ArrayList<Etudiant>();

	            // Appel de la méthode de connexion
	            connexion = connexionDB();

	            java.sql.Statement request = (java.sql.Statement) connexionDB().createStatement();
	            ResultSet response = request.executeQuery("SELECT * FROM etudiant");

	            // Boucler sur les donnees trouvees
	            while (response.next()) {
	            	Etudiant data = new Etudiant();

	                data.setId(response.getInt("id"));
	                data.setFirstname(response.getString("firstname"));
	                data.setLastname(response.getString("lastname"));
	                data.setBirthday(response.getDate("birthday"));

	                // Ajouter chaque information à la table des etudiants
	                listEtudiants.add(data);
	            }
	            System.out.println("Operation reussie !");
	            connexion.close(); // Fermer la connexion
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	            System.out.println("ERREUR : donnees non trouvees !");
	        }
	        return listEtudiants; // Retourner la liste des etudiant au niveau de la vue
	    }
	    
	    
	    
// *******************************************************************************************************
// **************************************__METHODE D'AJOUT DES DONNEES DANS LA BASE DE DONNEES.__*********
	    public String addNewEtud() {
	        int result = 0;
	        java.sql.Date birthSQL = (java.sql.Date) conversionDateToSQL(etudiant.getBirthday()); // Covertir la date au format sql avant l'insersion a la bd
	        try {
	            connexion = connexionDB();
	            PreparedStatement request = connexion.prepareStatement("INSERT INTO etudiant(firstname,lastname,birthday) VALUES(?,?,?)");
	            request.setString(1, etudiant.getFirstname());
	            request.setString(2, etudiant.getLastname().toUpperCase());
	            request.setDate(3, birthSQL); // Recupérer la variable de date de naissance au format SQL

	            result = request.executeUpdate();
	            System.out.println(Etudiant.getNextId()+" - L'etudiant (e) " + etudiant.getFirstname() + " " + etudiant.getLastname().toUpperCase() + " a ete ajoute (e).");
	            connexion.close();
	        } catch (Exception e) {
	            System.out.println(e);
	            System.out.println("ERREUR : impossible d'ajouter l'etudiant ");
	        }
	        if (result != 0)
	            return "index.xhtml?faces-redirect=true\"";
	        else
	            return "add.xhtml?faces-redirect=true";
	    }

	    // CONVERSION DE LA DATE RECUE VIA LE FORMULAIRE EN DATE SQL POUR L'INSERTION
	    // DANS LA BD
	    public java.sql.Date conversionDateToSQL(java.util.Date calendarDate) {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        String dateString = format.format(calendarDate);
	        return java.sql.Date.valueOf(dateString);
	    }
	    
	    
	    
// ***********************************************************************************************************
// **************************************__METHODE DE SUPPRESSION D'ETUDIANT.__*******************************
	    public void deleteEtudiant(int idEtud, String firstname, String lastname) { // Les deux derniers parametres n'ont pas d'effet - c'est pour afficher le  nom de l'etudiant supprimé
	        try {
	            connexion = connexionDB();
	            PreparedStatement request = connexion.prepareStatement("DELETE FROM etudiant WHERE id = " + idEtud);

	            request.executeUpdate();
	            System.out.println(idEtud + " - L'etudiant (e) " + firstname + " " + lastname + " a ete supprime (e).");
	        } catch (Exception e) {
	            System.out.println(e);
	            System.out.println("Aucun (e) etudiant (e) n'a ete supprime (e) ");
	        }
	    }
	    
	    
	      
// *********************************************************************************************************
// **************************************__METHODE DE MISE D'ETUDIANT.__************************************
	    public String updatePage(int updateById) {  // Parametre representant le futur Id au niveau de la vue.
	        Etudiant etudiant = null;
	        try {
	            connexion = connexionDB();
	            Statement request = connexionDB().createStatement();
	            ResultSet response = request.executeQuery("SELECT * FROM etudiant WHERE id = " + (updateById));

	            response.next();
	            etudiant = new Etudiant();
	            etudiant.setId(response.getInt("id"));
	            etudiant.setFirstname(response.getString("firstname"));
	            etudiant.setLastname(response.getString("lastname"));
	            etudiant.setBirthday(response.getDate("birthday"));
	            System.out.println(response.getDate("birthday"));

	            _SESSION.put("updated", etudiant); // Inject les informations à modifier dans le formulaire de modification via la variable globale _SESSION 
	            connexion.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "/update.xhtml?faces-redirect=true"; // Redirection vers la page de mise à jour
	    }

	    public String updateEtudiant(Etudiant etud) {
	        java.sql.Date birthSQL = (java.sql.Date) conversionDateToSQL(etud.getBirthday());
	        try {
	            connexion = connexionDB();
	            PreparedStatement request = connexion.prepareStatement("UPDATE etudiant SET firstname = ?, lastname = ?, birthday = ? WHERE id = ?");

	            // Mofifier les information prénom, nom et date de naissance
	            request.setString(1, etud.getFirstname());
	            request.setString(2, etud.getLastname().toUpperCase());
	            request.setDate(3, birthSQL);
	            request.setInt(4, (int) etud.getId());

	            request.execute();
	            // Afficher les infos de l'etudiant modifié au niveau de la console
	            System.out.println("Les donnees de l'etudiant ont ete mises a jour - : \n" + etud.getId() + "\n" + "\n"
	                    + etud.getFirstname() + "\n" + etud.getLastname().toUpperCase() + "\n" + birthSQL);
	            connexion.close();
	        } catch (Exception e) {
	            System.out.println("ERREUR : Impossible de modier les donnees");
	        }
	        return "/index.xhtml?faces-redirect=true"; // Redirection vers la page principale pour voir les modification apportées
	    }



		/**
		 * @return the etudiant
		 */
		public Etudiant getEtudiant() {
			return etudiant;
		}



		/**
		 * @param etudiant the etudiant to set
		 */
		public void setEtudiant(Etudiant etudiant) {
			this.etudiant = etudiant;
		}
	    
	    
	    


}
