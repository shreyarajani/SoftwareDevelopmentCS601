addfolder() ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                            <form align="center" method="post" action="/process_adding_folder" name="form" target="_top">
                              <fieldset>
                                 <legend align="center">Add Folder</legend>

                                <label for="folder">Folder Name:</label>
                                <input type="text" id="folder" name="folder" placeholder="Folder Name" required />

                                </fieldset>
                               <input type="submit" value="Add" />
                               <form>
                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>