showallcontacts(contacts) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <div align="center">All Contacts: <br/>
                          <a href="/addcontact">Add Contact</a> <div><br/>
                            <table class="table">
                            <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th></th>
                            <th></th>
                            </tr>
                            $contacts:{contact|
                            <tr>
                            <td>$contact.name$</td>
                            <td>$contact.email$</td>
                            <td><a href="/editcontact?id=$contact.email$">Edit</a></td>
                            <td><a href="/deletecontact?id=$contact.email$"> Delete</a></td>
                            </tr>
                            }$
                            </table>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>