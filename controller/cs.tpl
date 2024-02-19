using Microsoft.AspNetCore.Mvc;
using #project#.Models;
using System.Collections.Generic;

namespace #namespace#{
    public class #modelName#Controller : Controller
    {
        public IActionResult Liste()
        {
            #modelName# objInstance = new #modelName#();
            List<#modelName#> all = objInstance.getAll();
            return View(all);
        }
        
        public IActionResult Create()
        {
            #modelName# obj = new #modelName#();
            #fk#
            return View();
        }

        [HttpPost]
        public IActionResult Create(#modelName# obj)
        {
            obj.insert();
            return RedirectToAction("Liste");
        }

        public IActionResult Update(int id)
        {
            #modelName# objInstance = new #modelName#();
            #modelName# selected#modelName# = objInstance.getById(id);
            return View(selected#modelName#);
        }

        [HttpPost]
        public IActionResult Edit(#modelName# obj)
        {
            obj.update();
            return RedirectToAction("Liste");
        }

        public IActionResult Delete(int id)
        {
            #modelName# objInstance = new #modelName#();
            objInstance.delete(id);
            return RedirectToAction("Liste");
        }
    }
}
