@model #modelName#

<h2>Ajouter un Nouveau #modelName#</h2>

<form asp-action="Create" method="post">

    #champs#

    <div class="form-group">
        <button type="submit" class="btn btn-primary">Enregistrer</button>
    </div>
</form>
