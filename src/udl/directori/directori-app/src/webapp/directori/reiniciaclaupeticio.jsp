<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<% 
	response.setContentType("text/html; charset=UTF-8");
%>

<link rel="stylesheet" href="css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link rel="stylesheet" href="css/directori-activa.css" rel="stylesheet" />
<meta name="viewport" content="width=device-width, minimum-scale=0.9999, user-scalable=yes">
<script type="text/javascript" src="/library/js/jquery/jquery-1.9.1.min.js"></script>
<script>
	$(document).ready (function (){
		$(".myForm").submit (function (){
			$(".sendButton").css ("display","none");
			$(".processButton").css ("display","inline-block");
		});
	});
</script>

<f:loadBundle basename="es.udl.asic.tool.directori.bundle.Messages" var="msgs"/>
<f:view>
	<div class="row headerudl">
		<div class="col-xs-12 col-sm-6 col-md-6">
			<img class="img-responsive" src="image/logo-udl.png" />
		</div>
		
		<h:form styleClass="myform">
		<div class="col-xs-12 col-sm-6 col-md-6"  >
			<div class="langbar">
				[<h:commandLink  action="#{RecordaTool.canviaIdiomaActivacioAluCa}" value="#{msgs.activaalucatala}"  />]
				[<h:commandLink  action="#{RecordaTool.canviaIdiomaActivacioAluEs}" value="#{msgs.activaaluesp}" />]
				[<h:commandLink  action="#{RecordaTool.canviaIdiomaActivacioAluEng}" value="#{msgs.activaalueng}" />]
			</div>
		</div>

	</h:form>
	</div>
	
	
	<div class="container-fluid">
		<h1><h:outputText value="#{msgs.canviclautitle}" /></h1>

		<h:panelGroup rendered="#{RecordaTool.errorMsg != null && RecordaTool.errorMsg != ''}" styleClass="col-xs-12 alert alert-danger">
		 	<h:outputText escape="false" value="#{RecordaTool.errorMsg}" />
		</h:panelGroup>		
		<h:panelGroup rendered="#{RecordaTool.validToken==false}" styleClass="col-xs-12 alert alert-danger"> 
			<h:outputText escape="false" value="#{msgs.canviclaureinicianovalid}" />
			<h:form>
					<div class="form-group">
			  			<h:commandButton styleClass="btn btn-primary sendButton" action="#{RecordaTool.goToReinicia}" value="#{msgs.canviclaubotoenvia}"/>
			  		</div>
			  	</h:form>
		</h:panelGroup>
		
		<p><h:outputText rendered="#{RecordaTool.validToken==true}" escape="false" value="#{RecordaTool.changeWelcomeMessage}" /></p>
		<p><h:outputText rendered="#{RecordaTool.validToken==true}" escape="false" value="#{msgs.canviclaureiniciaintro}" /></p>
		<h:form styleClass="myForm" rendered="#{RecordaTool.validToken==true}">
						
				
				<div class="row form-group form-group-lg">
		   			<label class="col-sm-8"><h:outputText value="#{msgs.canviclaureinicialabel1}" /></label>
		    		 <div class="col-sm-8">
		    			<h:inputSecret styleClass="form-control" value="#{RecordaTool.clau}" />
		    		</div>
			  	</div>
			  	
			  	<div class="row form-group form-group-lg">
		   			<label class="col-sm-8"><h:outputText value="#{msgs.canviclaureinicialabel2}" /></label>
		    		 <div class="col-sm-8">
		    			<h:inputSecret styleClass="form-control" value="#{RecordaTool.clauRepeticio}" />
		    		</div>
			  	</div>
		  		<div class="form-group">
			  		<div class="btn btn-primary processButton disabled" style="display:none" ><i class="glyphicon glyphicon-repeat normal-right-spinner"></i> Processant... </div>
			  		<h:commandButton styleClass="btn btn-primary sendButton" action="#{RecordaTool.canviaPassword}" value="#{msgs.canviclaureiniciabutton}"/>
			  	</div>
	 	</h:form> 
	</div>
</f:view>
<script>window.parent.postMessage('hello','https://credencials.udl.cat');</script>