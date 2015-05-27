viewemail(email,attachments) ::= <<

$mailboxTop()$
                    <div class="container-fluid">
                      <div class="row-fluid">
                        $mailboxLeft()$

                        <div class="span9">
                          <div class="hero-unit">
                          <h4>$email.subject$</h4>
                          <small>
                          <strong>From:</strong> $email.from$
                          <br/>
                          <strong>To:</strong> $email.to$
                          <br/>
                          <if(email.cc)>
                          <strong>cc:</strong> $email.cc$
                          <endif>
                          <br/>
                          <strong>Date:</strong> $email.date$
                          <br/>
                          <strong>Subject:</strong> $email.subject$
                          <br/>
                          <strong>Attachments:</strong> <br/>
                          $attachments:{attachment|
                          <a href="$attachment.filepath$" download="$attachment.filename$">$attachment.filename$ - $attachment.size$</a>
                          <br/>
                          }$
                          <br/>
                          <strong>Body:</strong><br/>$email.body$
                          </small>
                            <br/>
                           <a class="btn btn-default" href="/reply?id=$email.mailid$" role="button">Reply</a>
                            <a class="btn btn-default" href="/forward?id=$email.mailid$" role="button">Forward</a>

                            </div>
                          </div>
                        </div><!--/span-->
                </div><!--/row-->

>>