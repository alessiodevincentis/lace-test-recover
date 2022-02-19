function skinPie() {
    this.cfg.shadow = true;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#03A9F4', '#3F51B5', '#4CAF50', '#FFC107', '#FF8080', '#ff355e',
        '#FF8141', '#89a8a5', '#004182', '#3ff18b', '#26a67e', '#067162', '#fbdffa', '#00253d'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77',
        }
    };
}