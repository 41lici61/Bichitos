using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Media;
using Bichitos.Models;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;

namespace Bichitos.ViewModels;

public partial class MainWindowViewModel : ViewModelBase
{
    //decorador, privado, minuscula, sin get ni set (el observavbleproperty los implementa automaticamnete)
    [ObservableProperty]
    private string mensaje = string.Empty;
    
    [ObservableProperty] private bool avanzadas = false;

    [ObservableProperty] private List<Models.Bichitos> bichitos= new();
    
    [ObservableProperty] 
    private Models.Bichitos bichito = new Models.Bichitos();
    
    [ObservableProperty]
    private Models.Bichitos bichitoSeleccionado = new Models.Bichitos();

    public MainWindowViewModel()
    {
        CargarBichitos();
        CargarCombo();
    }

    [RelayCommand]
    public void ComprobarCumple()
    {
        CheckDate();
    }
    
    private bool CheckDate()
    {
        if (bichito.Cumple > DateTime.Today)
        {
            mensaje = "¿Aún no ha nacido?";
            return false;
        }
        else
        {
            mensaje = "";
            return true;
        }
    }

    private void CargarBichitos()
    {
        Models.Bichitos bichito = new Models.Bichitos();
        bichito.Nombre = "Martín";
        bichito.Color = "Rosa";
        bichito.Cumple = Convert.ToDateTime("10/12/2003");
        bichito.Estado = true;
        bichito.evolucion = 4;
        bichitos.Add(bichito);
        
        Models.Bichitos bichito2 = new Models.Bichitos();
        bichito2.Nombre = "Daniel";
        bichito2.Color = "Marrón";
        bichito2.Cumple = DateTime.Today;
        bichito.Estado = true;
        bichito.evolucion = 1;
        bichitos.Add(bichito2);
    }

    [RelayCommand]
    public void CargarBichitoSeleccionado()
    {
        Bichito = BichitoSeleccionado;
    }
    
    public ObservableCollection<string> Lista { set; get; } = new()
    {
        "Rosa", "Fucsia", "Perla", "topacio", "Rojo", "Marrón", "Negro"
    };
    
    private void CargarCombo()
    {
        Lista = new ObservableCollection<string>()
        {
            "Rosa", "Fucsia", "Perla", "topacio", "Rojo", "Marrón", "Negro"
        };
        Bichito.Color = Lista[0];
    }

}

