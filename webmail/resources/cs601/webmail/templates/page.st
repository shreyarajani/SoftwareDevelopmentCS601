page(title, body) ::= <<
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!--  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"> -->

   <link rel="stylesheet" href="/boot/css/bootstrap.min.css">
   <link rel="stylesheet" href="/boot/css/base.css">
   <link rel="stylesheet" href="/boot/css/bootstrap-responsive.min.css">

   <script type="text/javascript" src="/boot/js/bootstrap.min.js"></script>
   <script type="text/javascript" src="/boot/js/base.js"></script>
   <script type="text/javascript" src="/boot/js/jquery-1.11.1.min.js"></script>


    <style type="text/css">
           body {
             padding-top: 60px;
             padding-bottom: 40px;
           }
           .sidebar-nav {
             padding: 9px 0;
           }

           @media (max-width: 980px) {
             /* Enable use of floated navbar text */
             .navbar-text.pull-right {
               float: none;
               padding-left: 5px;
               padding-right: 5px;
             }
           }
         </style>

<title>$title$</title>
</head>

<body>
<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <font size="6">
          <a class="brand" href="/">WebMail</a> </font>
        </div>
      </div>
    </div>
      $body$
      <hr>
      <footer>
        <p align="center"> WebMail Project 2015</p>
        <p align="center">&copy; CS-601 USFCA</p>
      </footer>

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/boot/js/jquery.js"></script>
    <script src="/boot/js/bootstrap-transition.js"></script>
    <script src="/boot/js/bootstrap-alert.js"></script>
    <script src="/boot/js/bootstrap-modal.js"></script>
    <script src="/boot/js/bootstrap-dropdown.js"></script>
    <script src="/boot/js/bootstrap-scrollspy.js"></script>
    <script src="/boot/js/bootstrap-tab.js"></script>
    <script src="/boot/js/bootstrap-tooltip.js"></script>
    <script src="/boot/js/bootstrap-popover.js"></script>
    <script src="/boot/js/bootstrap-button.js"></script>
    <script src="/boot/js/bootstrap-collapse.js"></script>
    <script src="/boot/js/bootstrap-carousel.js"></script>
    <script src="/boot/js/bootstrap-typeahead.js"></script>

  </body>
</html>

>>