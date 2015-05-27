addcontact() ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                            <form align="center" method="post" action="/add_contact" name="form" target="_top">
                              <fieldset>
                                 <legend align="center">Add Contact</legend>

                                <label for="name">Name:</label>
                                <input type="text" id="name" name="name" placeholder="Contact Name" required />

                                <label for="email">Email:</label>
                                <input type="email" id="email" name="email" placeholder="Contact Email" required />

                                </fieldset>
                               <input type="submit" value="Add" />
                               <form>
                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>