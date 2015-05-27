editcontact(contact) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                          <form class="form-horizontal" method="post" action="/update_contact" name="form" id="form" target="_top">

                            <div class="form-group">
                              <label for="name">Name: </label>
                             <input style="width:100%" type="text" id="name" name="name" value="$contact.name$" required/>
                            </div>

                            <div class="form-group">
                             <label for="email">Email: </label>
                             <input style="width:100%" type="email" id="email" name="email" value="$contact.email$" readonly/>
                             </div>

                            <button type="submit" class="btn btn-default">Update</button>
                          </form>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>