package es.udl.asic.tool.directori;

import es.udl.asic.api.app.directori.DirectoryService;
import es.udl.asic.api.app.directori.DadesPersonals;
import es.udl.asic.api.app.directori.AccountService; 


import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.event.api.UsageSession;


import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.model.SelectItem;
import javax.faces.component.UIComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sakaiproject.email.cover.EmailService;
import javax.servlet.http.HttpServletRequest;


public class RecordaTool{
	
	private DirectoryService directoryService;
	private AccountService accountService;
	private ResourceBundle messageBundle;
	private String errorMsg = "";
	private String norecordoInfo = "";
	private String canviaclauInfo = "";
	private String clau = "";
	private String clauRepeticio = "";
	private DadesPersonals userToChange;
	private boolean validToken = false;
	private String lastToken = "";
			
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	//Data for view-model
	private String email="";
	private String username ="";
	
	public void setDirectoryService(DirectoryService ds){
		directoryService = ds;
	}
	
	public DirectoryService getDirectoryService(){
		return directoryService;
	}
	
	public void setAccountService (AccountService accountService){
		this.accountService = accountService;
		//accountService.test();
		String bundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
     	messageBundle = ResourceBundle.getBundle(bundleName);
     	resetPantalles();
	}
	
	public AccountService getAccountService (){
		return this.accountService;
	}
	
	public String getClau () {
		return this.clau;
	}
	
	public void setClau (String clau) {
		this.clau = clau;
	}
	
	public String getClauRepeticio () {
		return this.clauRepeticio;
	}
	
	public void setClauRepeticio (String clauRepeticio) {
		this.clauRepeticio = clauRepeticio;
	}

	
	public boolean getValidToken () {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     	String token = httpServletRequest.getParameter("token");
     	String ip = getRemoteAddress(httpServletRequest);
		String agent = httpServletRequest.getHeader("User-agent");
		
     	if (token != null && !"".equals(token) && !lastToken.equals(token)) {
     		lastToken = token;
     		userToChange = accountService.checkForToken (token); 
     		validToken = (userToChange != null);
     		
     		if (validToken) {
     			accountService.desaEvidencia ("RESETCLAU_SOLICITUD_TOKEN_VALID ",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
     		} else {
     			accountService.desaEvidencia ("RESETCLAU_SOLICITUD_TOKEN_NO_VALID ",null,token,null,ip,agent);
     		}
     		
     		System.out.println ("Reinicialitzem el token " + token + " valid: " + validToken);
     	}
     	
		return validToken;
	}
	
	public String getChangeWelcomeMessage () {
		return messageBundle.getString ("canviclaureiniciahello") + " <b>" + userToChange.getNom()  + "</b>, " + messageBundle.getString ("canviclaureiniciaexplain") + " <b>" + userToChange.getUid() + "</b>";  
	}
	
	public String canviaPassword () {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
		String ip = getRemoteAddress(httpServletRequest);
		String agent = httpServletRequest.getHeader("User-agent");
		
		 
		if (!"".equals(clauRepeticio) && !"".equals(clau)){
			System.out.println ("Estan plenes");
			if (clau.equals(clauRepeticio)) {
				if (validatePassword(clau)) {
					if (validToken && userToChange!=null) {
						if (accountService.canviaClau (userToChange.getUid(),clau)){
							accountService.desaEvidencia ("RESETCLAU_OK",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
							validToken=false;
							return "reiniciaclaupeticiok";
							
						} else {
							accountService.desaEvidencia ("RESETCLAU_ERRORDESANT",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
							errorMsg = messageBundle.getString ("canviclaureiniciaerrordesant");
						}
					} else {
						accountService.desaEvidencia ("RESETCLAU_DESA_TOKEN_NO_VALID",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
						errorMsg = messageBundle.getString ("canviclaureinicianovalid");
						System.out.println ("No valid");
					}
				} else {
					accountService.desaEvidencia ("RESETCLAU_CLAUNOVALIDA",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
					errorMsg = messageBundle.getString ("canviclaureiniciaerrornopatro");
					System.out.println ("Password no valid");
				}
				 
			 } else {
				 accountService.desaEvidencia ("RESETCLAU_NOCOINCIDEIXEN",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
				 errorMsg = messageBundle.getString ("canviclaureiniciaerrornocoincideixen");
				 System.out.println ("Claus diferents");
			 }
		 } else {
			 accountService.desaEvidencia ("RESETCLAU_CLAUBUIDA",null,userToChange.getUid(),userToChange.getCorreuAlternatiu(),ip,agent);
			 errorMsg = messageBundle.getString ("canviclaureiniciaerrorbuida");
			 System.out.println ("Estan buides");
		 }
		
		 return "reiniciaclaupeticio";
	}
	
	
	public String test (){
		System.out.println ("Canvia");
		return "reiniciaclauactiva";
	}
	
	public String recordaUsuari () {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
		String ip = getRemoteAddress(httpServletRequest);
		String agent = httpServletRequest.getHeader("User-agent");
		
		
		if (!validateEmail (email)) {
			norecordoInfo = messageBundle.getString ("recordatoriintro");
			errorMsg = messageBundle.getString("recordatorierroremailnovalid");
			accountService.desaEvidencia ("RECORDAUSUARI_NOVALID",null,null,email,ip,agent);
			return "norecordousuari";
		}
		
		List <String> noms = accountService.cercaPerEmailAlternatiu (email);
		
		
		if (noms.size() > 0 ) {
			String evidenciaLogin = "";
			String subject = messageBundle.getString("recordatorisubject");
			String cos = messageBundle.getString ("recordatoribodycap") +"\n";
			cos = cos + ((noms.size() == 1) ? messageBundle.getString ("recordatoribodymigunic") : messageBundle.getString ("recordatoribodymigmultiple")) + "\n\n"; 
			for (String item : noms) {
				cos = cos + "\t" + item + "\n\n";
				evidenciaLogin = evidenciaLogin + item + "\n"; 
			}
			cos = cos + messageBundle.getString ("recordatoribodypeu");
		
			enviaMissatge (subject, cos, email);
			
			
			
			accountService.desaEvidencia ("RECORDAUSUARI_ENVIAT",null,evidenciaLogin,email,ip,agent);
			
			
			return "norecordousuariok";
		} else {
			errorMsg = messageBundle.getString ("recordatorierroremailinexistent");
			norecordoInfo = messageBundle.getString ("recordatoriintroinexistent");
			accountService.desaEvidencia ("RECORDAUSUARI_NOTROBAT",null,null,email,ip,agent);
			return "norecordousuari";
		}
	}
	
	public String reiniciaClau () {
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
		String ip = getRemoteAddress(httpServletRequest);
		String agent = httpServletRequest.getHeader("User-agent");
		
		if (!validateUsername (username)){
			canviaclauInfo = messageBundle.getString ("canviclauintrousuariinexistent");
			errorMsg = messageBundle.getString("canviclauerrorusuariinexistent");
			accountService.desaEvidencia ("CANVICLAU_NOVALID",null,username,null,ip,agent);
			return "reiniciaclau";
		}
		
		List <DadesPersonals> usuaris = accountService.cercaPerNomUsuari (username);
		
		if (usuaris == null) { //Usuari inexistent 
			errorMsg = messageBundle.getString ("canviclauerrorusuariinexistent");
			canviaclauInfo = messageBundle.getString ("canviclauintrousuariinexistent");
			accountService.desaEvidencia ("CANVICLAU_USURINOTROBAT",null,username,null,ip,agent);
		} else if (usuaris.size() > 0 ) { // No hi ha vinculats correus
		
			String subject = messageBundle.getString("canviclausubject");
			
			//Generem un uid i el desem
			String token = accountService.creaToken (username);
			
			String cos = messageBundle.getString ("canviclaubodycap") +"\n";
			cos = cos + "https://credencials.udl.cat/valida.php?token=" +token;  
			cos = cos + messageBundle.getString ("canviclaubodypeu");
			
			boolean sent = false;
			
			for (DadesPersonals item : usuaris) {
				
				if ("ACTIU".equals(item.getEstat())){ //Si alguna de les adreces de correu diferent de noactiu tirarem endavant sino creiem que falta activar
					enviaMissatge (subject, cos, item.getCorreuAlternatiu());
					accountService.desaEvidencia ("CANVICLAU_ENVIAT",null, username, item.getCorreuAlternatiu() , ip, agent);
					sent = true;
				}
			}
			
			if (!sent) { // No ha trobat cap email i per tant només hi havia no actius 
				accountService.desaEvidencia ("CANVICLAU_ENCARANOACTIVAT",null, username, null , ip,agent);
				return "reiniciaclauactiva";
			}
			
			return "reiniciaclauok";
		} else {
			errorMsg = messageBundle.getString ("canviclauerroremailinexistent");
			canviaclauInfo = messageBundle.getString ("canviclauintroemailinexistent");
			accountService.desaEvidencia ("CANVICLAU_MAILNOTROBAT",null,username,null,ip,agent);
		}
		
		return "reiniciaclau";
	}
	
	public void canviaIdioma (String idioma) {
		messageBundle = ResourceBundle.getBundle("es.udl.asic.tool.directori.bundle.Messages",new Locale(idioma));
		
		FacesContext context = FacesContext.getCurrentInstance();
		//context.getViewRoot().setLocale (new Locale(idioma));
		UIViewRoot view = context.getViewRoot();
		view.setLocale (new Locale(idioma));
	}
	
	public String canviaIdiomaActivacioAluCa () {
		canviaIdioma("ca");
		return "activaAlu";
	}
	
	public String canviaIdiomaActivacioAluEs () {
		canviaIdioma("es");
		return "activaAlu";
	}
	
	public String canviaIdiomaActivacioAluEng () {
		canviaIdioma("en");
		return "activaAlu";
	}
	
	public String goToActiva () {
		resetPantalles();
		return "activamenu";
	}
	
	public String goToRecorda () {
		resetPantalles();
		return "norecordousuari";
	}
	
	public String goToReinicia () {
		resetPantalles ();
		return "reiniciaclau";
	}
	
	public String goToMain() {
		return "assistenciausuari";
	}
	
	public String getEmail (){
		return this.email;
	}
	
	public String getUsername (){
		return this.username;
	}
	
	public String getErrorMsg(){
		return errorMsg;
	}
	
	public String getNorecordoInfo(){
		return norecordoInfo;
	}
	
	public String getCanviaclauInfo(){
		return canviaclauInfo;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public void setUsername (String username) {
		this.username= username;
	}
	
	private void enviaMissatge(String subject, String missatge, String email){
		String from= "noreply@cv.udl.cat";
		EmailService.send(from,email,subject,missatge,null,null,null);
			
	}
	
	private boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}
	
	private boolean validateUsername(String username){
		if (contains(".*(\\.|\\_|\\-|\\(|\\)|\\[\\]\\:\\;\\,\\ç\\{\\}\\*\\^\\!\\\"\\#\\$\\&\\/\\=\\<\\>).*",username)==1){
			return false;
		}
		
		return true;
	}
	
	private boolean validatePassword(String password){
		int number = contains(".*([a-z])+.*",password) +  contains(".*([A-Z])+.*",password) + contains(".*([0-9])+.*",password)  + 
					 contains(".*(\\.|\\_|\\-|\\(|\\)|\\[\\]\\:\\;\\,\\ç\\{\\}\\*\\^\\!\\\"\\#\\$\\&\\/\\=\\<\\>)+.*",password);
		return  (number >=3 && password.length() >= 8);
	}
	
	public static String getRemoteAddress(HttpServletRequest req) {
	    String ipAddress = req.getHeader("X-FORWARDED-FOR");
	    if (ipAddress != null) {
	        ipAddress = ipAddress.replaceFirst(",.*", "");  // cares only about the first IP if there is a list
	    } else {
	        ipAddress = req.getRemoteAddr();
	    }
	    return ipAddress;
	}
	
	private void resetPantalles () {
		errorMsg = "";
		username = "";
		email = "";
		validToken = false;
	  	norecordoInfo = messageBundle.getString ("recordatoriintro");
     	canviaclauInfo = messageBundle.getString ("canviclauintro");
     	System.out.println ("Reinicio pantalles");
	}
	
	
	
	private int contains(String regexpstring,String key){
		Pattern regexp = Pattern.compile(regexpstring);
		
		if (regexp.matcher(key).matches())
			return 1;
		else return 0;
	}
	
	
}