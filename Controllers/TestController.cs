using Microsoft.AspNetCore.Mvc;
using Scafolding.Models;
using System.Collections.Generic;

namespace Scafolding.Controllers{
    public class TestController : Controller
    {
        public IActionResult Liste()
        {
            Test objInstance = new Test();
            List<Test> all = objInstance.getAll();
            return View(all);
        }
        
        public IActionResult Create()
        {
            Test obj = new Test();
            var dept = obj.fkDept(); 

            return View();
        }

        [HttpPost]
        public IActionResult Create(Test obj)
        {
            obj.insert();
            return RedirectToAction("Liste");
        }

        public IActionResult Update(int id)
        {
            Test objInstance = new Test();
            Test selectedTest = objInstance.getById(id);
            return View(selectedTest);
        }

        [HttpPost]
        public IActionResult Edit(Test obj)
        {
            obj.update();
            return RedirectToAction("Liste");
        }

        public IActionResult Delete(int id)
        {
            Test objInstance = new Test();
            objInstance.delete(id);
            return RedirectToAction("Liste");
        }
    }
}

