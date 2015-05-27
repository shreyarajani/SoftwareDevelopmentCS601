showallfolders(folders) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          All Folders: <br/>

                            $folders:{folder|
                            <a href="/showfolder?id=$folder.folder$"> $folder.folder$</a> &nbsp&nbsp&nbsp&nbsp
                            <a href="/deletefolder?id=$folder.folder$"> Delete</a>
                            <br/>
                            }$
                            <br/>
                            <a href="/addfolder">Add Folder</a>
                            <font color="red"> <br/>
                                Warning: Deleting a folder will delete all the emails inside it!
                            </font>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>