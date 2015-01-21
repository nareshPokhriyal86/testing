<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-41865102-1', $(location).attr('hostname'));
  ga('send', 'pageview', {
		  'page': $(location).attr('pathname'),
		  'title': document.title,
		  'dimension1': '<s:property value="#session.sessionDTO.userId"/>',
		  'dimension2': '<s:property value="#session.sessionDTO.userName"/>'
		});
  ga('set', 'dimension1', '<s:property value="#session.sessionDTO.userId"/>');
  ga('set', 'dimension2', '<s:property value="#session.sessionDTO.userName"/>');
</script>