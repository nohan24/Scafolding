using Microsoft.AspNetCore.Mvc;
using #modelPackage#;
using System.Collections.Generic;

public class #modelName#Controller : Controller
{
    public IActionResult Liste#modelName#()
    {
        #modelName# objInstance = new #modelName#();
        List<#modelName#> all#modelName#s = objInstance.getAll();
        return View(all#modelName#s);
    }
    
    public IActionResult Create()
    {
        return View("Create#modelName#");
    }

    [HttpPost]
    public IActionResult Create(#modelName# obj)
    {
        obj.insert();
        return RedirectToAction("Liste#modelName#");
    }

    public IActionResult Update#modelName#(int id)
    {
        #modelName# objInstance = new #modelName#();
        #modelName# selected#modelName# = objInstance.getById(id);
        return View(selected#modelName#);
    }

    [HttpPost]
    public IActionResult Edit(Ab#modelName#o obj)
    {
        obj.update();
        return RedirectToAction("Liste#modelName#");
    }

    [HttpPost]
    public IActionResult Delete(int id)
    {
        #modelName# objInstance = new #modelName#();
        objInstance.delete(id);
        return RedirectToAction("Liste#modelName#");
    }
}
