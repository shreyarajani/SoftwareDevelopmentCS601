compose(contacts) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">

                          <form class="form-horizontal" method="post" action="/send_email" name="form" id="form" target="_top" enctype="multipart/form-data">

                            <div class="form-group">
                              <label for="mailto">To: </label>
                             <input style="width:100%" type="text" id="mailto" name="mailto" list="field" required />
                             <datalist id="field">
                                $contacts:{contact|
                                <option value="$contact.name$ <$contact.email$>">
                                }$
                               </datalist>
                            </div>

                            <div class="form-group">
                             <label for="mailcc">cc: </label>
                             <input style="width:100%" type="text" id="mailcc" list="field" name="mailcc" />
                                <datalist id="field">
                                $contacts:{contact|
                                <option value="$contact.name$ <$contact.email$>">
                                }$
                               </datalist>
                             </div>

                             <div class="form-group">
                             <label for="mailbcc">bcc: </label>
                             <input style="width:100%" type="text" id="mailbcc" name="mailbcc" list="field"/>
                                <datalist id="field">
                                $contacts:{contact|
                                <option value="$contact.name$ <$contact.email$>">
                                }$
                               </datalist>
                             </div>

                             <div class="form-group">
                             <label for="mailsubject">Subject: </label>
                             <input style="width:100%" spellcheck="true" type="text" id="mailsubject" name="mailsubject"/>
                             </div>

                             <div class="form-group">
                             <label for="mailbody">Body: </label>
                             <textarea style="width:100%" spellcheck="true" form="form" rows="10" name="mailbody" id="mailbody" name="mailbody"></textarea>
                             </div>

                            <div class="form-group">
                              <label for="file">File input</label>
                              <input type="file" id="file" name=file" multiple>
                            </div>

                            <button type="submit" class="btn btn-default">Send</button>
                          </form>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->
>>