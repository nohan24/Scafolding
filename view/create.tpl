@model #modelName#
@{
    ViewData["Title"] = "Create #modelName#";
}

<h2>Ajouter un nouveau #modelName#</h2>

<form asp-action="Create" method="post">

    #champs#

<br/>
    <div class="form-group mt-3">
        <button type="submit" class="btn btn-primary">Enregistrer</button>
    </div>
</form>
