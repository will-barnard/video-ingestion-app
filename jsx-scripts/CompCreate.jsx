var proj = app.proj;
var selection= proj.selection; 
var amount= selection.length;
var lowerThirdDir = "";

var mySelectedItems = [];
for (var i = 1; i <= app.project.numItems; i++){
    if (app.project.item(i).selected) {
        mySelectedItems[mySelectedItems.length] = app.project.item(i);
    }
}

for (var i = 0; i < mySelectedItems.length; i++){

    var mySelection = mySelectedItems[i];
    var newName = mySelection.split("//.");
    var titleName = String.parseInt(newName);

    var myComp = app.project.items.addComp(newName[0],mySelection.width, mySelection.height, mySelection.pixelAspect, mySelection.duration, 30);
    
    var myNewCell = myComp.layers.add(mySelection);



}