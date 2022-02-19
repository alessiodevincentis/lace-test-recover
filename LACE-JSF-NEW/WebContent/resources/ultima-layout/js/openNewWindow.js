function openNewWindow(page) {
	var open = window.open(page,"_blank");
	if (open == null || typeof(open)=="undefined")
	    alert("Attenzione! Disabilita il blocco popup!");	
}