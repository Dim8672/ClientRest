package ch.hearc.ig.asi.exercice4.utilitaire;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe helper pour construire des Urls JSF avec des paramètres GET
 * A l'instanciation de la classe on spécifie l'URL ou l'action à réaliser, puis s'il faut faire un redirect ou pas.
 * Ensuite on peut appeler la méthode param() pour ajouter des paramètres à faire passer
 * Une fois l'URL prête, on appelle navigateTo() pour récupérer la chaîne de caractère JSF correspondant au cas de navigation.
 * Source : https://github.com/codebulb/JsfRestfulPostRedirectGetViewScoped
 */
public class Url {
    private final String outcome;
    private boolean redirect;
    private final List<Parameter> parameters = new ArrayList<>();
    /**
     * Crée un objet avec l'URL de destination, sans redirection
     * @param outcome L'URL ou l'
     */
    public Url(String outcome) {
        this(outcome, false);
    }
    /**
     * Crée un objet avec l'URL de destination et s'il faut faire une redirection ou pas
     * @param outcome L'URL de destination
     * @param redirect Faut-il faire une redirection ou pas ?
     */
    public Url(String outcome, boolean redirect) {
        this.outcome = outcome;
        this.redirect = redirect;
    }
    /**
     * Configure l'instance pour faire une redirection
     * @return 
     */
    public Url redirect() {
        this.redirect = true;
        return this;
    }
    /**
     * Ajoute un paramètre GET à l'URL.
     * Si la valeur du paramètre est null, on ignore ledit paramètre.
     * @param name Le nom du paramètre
     * @param value La valeur du paramètre
     * @return L'instance Url pour pouvoir faire plusieurs appels à la suite
     */
    public Url param(String name, Object value) {
        if (name != null && value != null) {
            parameters.add(new Parameter(name, value.toString()));
        }
        return this;
    }
    /**
     * Génère la chaîne de caractères avec la cible de l'URL, le paramètres faces-redirect si besoin est, et les paramètres GET.
     * @return L'URL JSF
     */
    public String navigateTo() {
        if (!redirect && parameters.size() == 0) {
            // allows to return null
            return outcome;
        }
        if (outcome == null) {
            // ignore redirect and parameters
            return outcome;
        }
        StringBuilder ret = new StringBuilder(outcome);
        char operator = '?';
        if (redirect) {
            ret.append(operator);
            ret.append("faces-redirect=true");
            operator = '&';
        }
        for (Parameter parameter : parameters) {
            ret.append(operator);
            ret.append(parameter.name);
            ret.append('=');
            ret.append(parameter.value);
            operator = '&';
        }
        return ret.toString();
    }
    protected static class Parameter {
        private final String name, value;
        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}