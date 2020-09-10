import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var stations: UIPickerView!
    @IBOutlet weak var departureStationLabel: UILabel!
    @IBOutlet weak var arrivalStationLabel: UILabel!
    @IBOutlet weak var getTimesButton: UIButton!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var stationData: [Station] = [];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        getTimesButton.setTitle("Get Times", for: .normal);
        departureStationLabel.text = "Departure";
        arrivalStationLabel.text = "Arrival";
        
        stations.delegate = self;
        stations.dataSource = self;
    }
    
    @IBAction func getTimes(_ sender: Any) {
        presenter.onTimesRequested();
    }
}

extension ViewController :  UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2;
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return stationData.count;
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return stationData[row].name
    }
}

extension ViewController: ApplicationContractView {
    func setStations(stations: [Station]) {
        stationData = stations;
    }
    
    func getArrivalStation() -> Station {
        return stationData[stations.selectedRow(inComponent: 1)];
    }
    
    func getDepartureStation() -> Station {
        return stationData[stations.selectedRow(inComponent: 0)];
    }
    
    func showAlert(message: String) {
        let alert = UIAlertController(title: "Alert", message: message, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func setLabel(text: String) {
        label.text = text
    }
    
    
}
