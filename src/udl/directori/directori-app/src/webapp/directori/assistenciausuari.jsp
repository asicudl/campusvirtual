
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<% response.setContentType("text/html; charset=UTF-8");%>

<link rel="stylesheet" href="css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link rel="stylesheet" href="css/directori-activa.css" rel="stylesheet" />
<meta name="viewport" content="width=device-width, minimum-scale=0.9999, user-scalable=yes">
<script type="text/javascript" src="/library/js/jquery/jquery-1.9.1.min.js"></script>


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
		<h1><h:outputText value="#{msgs.assistenciaustitle}" /></h1>
		<h:form>
			<p><h:outputText value="#{msgs.assistenciausintro1}" /></p>
			<div class="form-group">
				<h:commandButton styleClass="btn btn-primary sendButton" action="#{RecordaTool.goToActiva}" value="#{msgs.canviclauactivaboto}"/>
			</div>
			<p><h:outputText value="#{msgs.assistenciausintro2}" /></p>
			<div class="form-group">
					<h:commandButton styleClass="btn btn-primary sendButton" action="#{RecordaTool.goToRecorda}" value="#{msgs.recordatorititle}"/>
			</div>
			<div class="form-group">
					<h:commandButton styleClass="btn btn-primary sendButton" action="#{RecordaTool.goToReinicia}" value="#{msgs.canviclautitle}"/>
			</div> 
		</h:form>
	</div>
</f:view>
<script>window.parent.postMessage('hello','https://credencials.udl.cat');</script>