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
            ViewData["data"] = all;
            return View();
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
            #modelName# obj = objInstance.getById(id);
            #fk#
            return View(obj);
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
