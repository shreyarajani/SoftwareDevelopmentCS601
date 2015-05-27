function valthisform()
{
    var checkboxs=document.getElementsByName("mailid");
    var okay=false;
    for(var i=0,l=checkboxs.length;i<l;i++)
    {
        if(checkboxs[i].checked)
        {
            okay=true;
        }
    }
    if(!okay)alert("Please check a checkbox");
    return false;
}
