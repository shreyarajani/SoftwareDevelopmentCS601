showfolder(emails, folder, pages) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <div align="center">
                          <b>$folder$</b>
                          <div>
                           <ul class="pagination">
                           $pages:{page|
                           <li><a href="/showfolder?page=$page$&id=$folder$">$page$</a></li>
                            }$
                           </ul>
                          <br/>

                            <form class="form-inline" style="display:inline;" method="post" action="/move_to_folder" name="form">
                              <select name="foldername" id="foldername">
                               <option disabled="disabled" selected="selected">Move to Folder</option>
                               <option value="$folder$_inbox" id="$folder$_inbox">Inbox</option>
                               <option value="$folder$_spam" id="$folder$_spam">Spam</option>
                               <option value="$folder$_trash" id="$folder$_trash">Trash</option>

                               </select>
                               <button type="submit" class="btn btn-default">Move</button>
                               <hr/>

                            <table class="table table-hover">
                            <tr>
                              <th width="3%"></th>
                               <th width="3%"></th>
                               <th width="20%">From <a href="/showfolder?fo=a-from&id=$folder$"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/showfolder?fo=d-from&id=$folder$"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                               <th width="35%">Subject <a href="/showfolder?fo=a-subject&id=$folder$"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/showfolder?fo=d-subject&id=$folder$"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                               <th width="15%">Date <a href="/showfolder?fo=a-date&id=$folder$"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/showfolder?fo=d-date&id=$folder$"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                               <th width="15%">Size <a href="/showfolder?fo=a-size&id=$folder$"><span class="glyphicon glyphicon-arrow-up"></span></a> <a href="/showfolder?fo=d-size&id=$folder$"><span class="glyphicon glyphicon-arrow-down"></span></a></th>
                               <th width="15%">View</th>
                            </tr>
                            $emails:{email|
                              <tr>
                                <td>
                                <div class="checkbox" required id="check" >
                                  <input type="checkbox" name="mailid" value="$email.mailid$">
                                </div>
                                </td>
                                <td></td>
                                <td>$email.from$</td>
                                <td>$email.subject$</td>
                                <td>$email.date$</td>
                                <td>$email.size$</td></a>
                                <td><a href="/viewemail?id=$email.mailid$">View</a>
                              </tr>
                              }$

                            </table>
                            </form>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>